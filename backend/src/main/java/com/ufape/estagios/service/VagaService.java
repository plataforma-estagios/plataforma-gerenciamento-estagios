package com.ufape.estagios.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ufape.estagios.dto.VagaRequestDTO;
import com.ufape.estagios.dto.VagaResponseDTO;
import com.ufape.estagios.exception.AccessDeniedException;
import com.ufape.estagios.exception.IdNotFoundException;
import com.ufape.estagios.mapper.VagaMapper;
import com.ufape.estagios.model.Empresa;
import com.ufape.estagios.model.Localizacao;
import com.ufape.estagios.model.StatusDaVaga;
import com.ufape.estagios.model.TipoVaga;
import com.ufape.estagios.model.Usuario;
import com.ufape.estagios.model.Vaga;
import com.ufape.estagios.repository.EmpresaRepository;
import com.ufape.estagios.repository.VagaRepository;

import jakarta.persistence.criteria.Predicate;

@Service
public class VagaService {
    @Autowired
    private VagaRepository vagaRepository;
    
    @Autowired
    private EmpresaRepository empresaRepository;

    @Transactional
    public VagaResponseDTO cadastrarVaga(VagaRequestDTO dto) {
        Empresa empresa = findEmpresaByUsuarioAutenticado();

        Vaga vaga = VagaMapper.toEntity(dto, empresa);
        vaga.setStatus(StatusDaVaga.EM_ABERTO);
        Vaga vagaSalva = vagaRepository.save(vaga);
        return VagaMapper.fromEntity(vagaSalva);
    }

    public List<VagaResponseDTO> listarVagasParaEstudantes(String area, String tipo, String local, String sortBy) {
        // Define a ordenação: padrão é data de publicação (mais recentes primeiro)
        Sort sort = Sort.by(Sort.Direction.DESC, sortBy != null ? sortBy : "dataPublicacao");

        Specification<Vaga> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // CRITÉRIO DE ACEITE: Somente vagas ativas
            predicates.add(cb.isTrue(root.get("ativa")));

            // CRITÉRIO DE ACEITE: Somente vagas onde o prazo não venceu
            predicates.add(cb.greaterThanOrEqualTo(root.get("prazoCandidatura"), LocalDate.now()));

            // Filtro opcional por Área de Conhecimento
            if (area != null && !area.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("areaConhecimento")), "%" + area.toLowerCase() + "%"));
            }

            // Filtro opcional por Tipo de Vaga (converte String para Enum)
            if (tipo != null && !tipo.isEmpty()) {
                try {
                    predicates.add(cb.equal(root.get("tipoVaga"), TipoVaga.valueOf(tipo.toUpperCase())));
                } catch (IllegalArgumentException ignored) {
                }
            }

            // Filtro opcional por Localização
            if (local != null && !local.isEmpty()) {
                try {
                    predicates.add(cb.equal(root.get("localizacao"), Localizacao.valueOf(local.toUpperCase())));
                } catch (IllegalArgumentException ignored) {
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return vagaRepository.findAll(spec, sort)
                .stream()
                .map(VagaMapper::fromEntity)
                .collect(Collectors.toList());
    }

    public List<VagaResponseDTO> listarVagasAtivas() {
        return vagaRepository.findByAtivaTrue()
                .stream()
                .map(VagaMapper::fromEntity)
                .collect(Collectors.toList());
    }

    public List<VagaResponseDTO> listarMinhasVagas() {
        Empresa empresa = findEmpresaByUsuarioAutenticado();

        return vagaRepository.findByEmpresa(empresa)
                .stream()
                .map(VagaMapper::fromEntity)
                .collect(Collectors.toList());
    }

    public VagaResponseDTO buscarVagaPorId(Long id) {
        Vaga vaga = findById(id);

        return VagaMapper.fromEntity(vaga);
    }

    @Transactional
    public VagaResponseDTO atualizarVaga(Long id, VagaRequestDTO dto) {
        Vaga vaga = findById(id);

        validarAcessoAVaga(vaga);

        vaga = VagaMapper.updateVaga(vaga, dto);
        Vaga vagaAtualizada = vagaRepository.save(vaga);
        return VagaMapper.fromEntity(vagaAtualizada);
    }

    @Transactional
    public void desativarVaga(Long id) {
        Vaga vaga = findById(id);
        
        validarAcessoAVaga(vaga);

        vaga.setAtiva(false);
        vaga.setStatus(StatusDaVaga.FECHADA);
        vagaRepository.save(vaga);
    }
    
    public Vaga findById(Long id) {
    	return vagaRepository.findById(id).orElseThrow(() -> new IdNotFoundException("Vaga"));
    }

    private Usuario getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Usuario) authentication.getPrincipal();
    }
    
    private Empresa findEmpresaByUsuarioAutenticado() {
    	Usuario usuario = getUsuarioAutenticado();
    	
    	Optional<Empresa> optionalEmpresa = empresaRepository.findByUsuario(usuario);
    	
    	return optionalEmpresa.orElseThrow(() -> new IdNotFoundException("Usuario"));
    }
    
    private void validarAcessoAVaga(Vaga vaga) {
    	Usuario usuarioLogado = getUsuarioAutenticado();
    	
    	if (!vaga.getEmpresa().getUsuario().getId().equals(usuarioLogado.getId())) {
            throw new AccessDeniedException("Você não tem permissão para gerenciar essa vaga");
        }
    }
}