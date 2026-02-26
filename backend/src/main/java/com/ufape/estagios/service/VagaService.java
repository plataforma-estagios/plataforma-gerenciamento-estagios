package com.ufape.estagios.service;

import com.ufape.estagios.dto.VagaRequestDTO;
import com.ufape.estagios.dto.VagaResponseDTO;
import com.ufape.estagios.exception.AccessDeniedException;
import com.ufape.estagios.exception.IdNotFoundException;
import com.ufape.estagios.mapper.VagaMapper;
import com.ufape.estagios.model.Usuario;
import com.ufape.estagios.model.UserRole;
import com.ufape.estagios.model.Vaga;
import com.ufape.estagios.model.TipoVaga;
import com.ufape.estagios.model.Localizacao;
import com.ufape.estagios.model.StatusDaVaga;
import com.ufape.estagios.repository.VagaRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VagaService {
    @Autowired
    private VagaRepository vagaRepository;

    @Transactional
    public VagaResponseDTO cadastrarVaga(VagaRequestDTO dto) {
        Usuario empresa = getUsuarioAutenticado();

        if (empresa.getRole() != UserRole.COMPANY) {
        	throw new AccessDeniedException("Only companies can create jobs");
        }

        Vaga vaga = VagaMapper.toEntity(dto, empresa);
        vaga.setStatus(StatusDaVaga.EM_ABERTO);
        Vaga vagaSalva = vagaRepository.save(vaga);
        return VagaResponseDTO.fromEntity(vagaSalva);
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
                .map(VagaResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<VagaResponseDTO> listarVagasAtivas() {
        return vagaRepository.findByAtivaTrue()
                .stream()
                .map(VagaResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<VagaResponseDTO> listarMinhasVagas() {
        Usuario empresa = getUsuarioAutenticado();

        if (empresa.getRole() != UserRole.COMPANY) {
            throw new AccessDeniedException("Only companies can list your jobs");
        }

        return vagaRepository.findByEmpresa(empresa)
                .stream()
                .map(VagaResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public VagaResponseDTO buscarVagaPorId(Long id) {
        Vaga vaga = vagaRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException("Job"));

        return VagaResponseDTO.fromEntity(vaga);
    }

    @Transactional
    public VagaResponseDTO atualizarVaga(Long id, VagaRequestDTO dto) {
        Usuario empresa = getUsuarioAutenticado();

        Vaga vaga = vagaRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException("Job"));

        if (!vaga.getEmpresa().getId().equals(empresa.getId())) {
        	throw new AccessDeniedException("You dont haver permission to update this job");
        }

        vaga = VagaMapper.updateVaga(vaga, dto);
        Vaga vagaAtualizada = vagaRepository.save(vaga);
        return VagaResponseDTO.fromEntity(vagaAtualizada);
    }

    @Transactional
    public void desativarVaga(Long id) {
        Usuario empresa = getUsuarioAutenticado();

        Vaga vaga = vagaRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException("Job"));

        if (!vaga.getEmpresa().getId().equals(empresa.getId())) {
            throw new AccessDeniedException("You dont have permission to disable this job");
        }

        vaga.setAtiva(false);
        vagaRepository.save(vaga);
    }

    private Usuario getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Usuario) authentication.getPrincipal();
    }
}