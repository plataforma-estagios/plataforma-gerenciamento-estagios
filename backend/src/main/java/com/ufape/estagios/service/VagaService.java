package com.ufape.estagios.service;

import com.ufape.estagios.dto.VagaRequestDTO;
import com.ufape.estagios.dto.VagaResponseDTO;
import com.ufape.estagios.mapper.VagaMapper;
import com.ufape.estagios.model.Usuario;
import com.ufape.estagios.model.UserRole;
import com.ufape.estagios.model.Vaga;
import com.ufape.estagios.repository.VagaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VagaService{
    @Autowired
    private VagaRepository vagaRepository;

    @Transactional
    public VagaResponseDTO cadastrarVaga(VagaRequestDTO dto){
        Usuario empresa = getUsuarioAutenticado();

        if(empresa.getRole() != UserRole.COMPANY){
            throw new RuntimeException("Apenas empresas podem cadastrar vagas");
        }

        Vaga vaga = VagaMapper.toEntity(dto, empresa);

        Vaga vagaSalva = vagaRepository.save(vaga);

        return VagaResponseDTO.fromEntity(vagaSalva);
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
            throw new RuntimeException("Apenas empresas podem listar suas vagas");
        }

        return vagaRepository.findByEmpresa(empresa)
                .stream()
                .map(VagaResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public VagaResponseDTO buscarVagaPorId(Long id) {
        Vaga vaga = vagaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaga não encontrada"));

        return VagaResponseDTO.fromEntity(vaga);
    }

    @Transactional
    public VagaResponseDTO atualizarVaga(Long id, VagaRequestDTO dto) {
        Usuario empresa = getUsuarioAutenticado();

        Vaga vaga = vagaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaga não encontrada"));

        if (!vaga.getEmpresa().getId().equals(empresa.getId())) {
            throw new RuntimeException("Você não tem permissão para editar esta vaga");
        }

        vaga = VagaMapper.updateVaga(vaga, dto);

        Vaga vagaAtualizada = vagaRepository.save(vaga);

        return VagaResponseDTO.fromEntity(vagaAtualizada);
    }

    @Transactional
    public void desativarVaga(Long id) {
        Usuario empresa = getUsuarioAutenticado();

        Vaga vaga = vagaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaga não encontrada"));

        if (!vaga.getEmpresa().getId().equals(empresa.getId())) {
            throw new RuntimeException("Você não tem permissão para desativar esta vaga");
        }

        vaga.setAtiva(false);
        vagaRepository.save(vaga);
    }

    private Usuario getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Usuario) authentication.getPrincipal();
    }

}