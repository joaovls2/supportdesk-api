package com.supportdesk.service;

import com.supportdesk.dto.LoginRequestDTO;
import com.supportdesk.dto.LoginResponseDTO;
import com.supportdesk.entity.Administrador;
import com.supportdesk.entity.Tecnico;
import com.supportdesk.entity.Usuario;
import com.supportdesk.exception.BusinessException;
import com.supportdesk.repository.AdministradorRepository;
import com.supportdesk.repository.TecnicoRepository;
import com.supportdesk.repository.UsuarioRepository;
import com.supportdesk.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final TecnicoRepository tecnicoRepository;
    private final AdministradorRepository administradorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UsuarioRepository usuarioRepository,
            TecnicoRepository tecnicoRepository,
            AdministradorRepository administradorRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.usuarioRepository = usuarioRepository;
        this.tecnicoRepository = tecnicoRepository;
        this.administradorRepository = administradorRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponseDTO loginUsuario(LoginRequestDTO dto) {

        Usuario usuario = usuarioRepository
                .findByEmail(dto.getEmail())
                .orElseThrow(() ->
                        new BusinessException("E-mail ou senha inválidos"));

        if (!passwordEncoder.matches(dto.getSenha(), usuario.getSenha())) {
            throw new BusinessException("E-mail ou senha inválidos");
        }

        Long empresaId = usuario.getEmpresa().getId();

        String token = jwtService.gerarToken(
                usuario.getEmail(),
                "USUARIO",
                empresaId
        );

        return new LoginResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                "USUARIO",
                empresaId,
                token
        );
    }

    public LoginResponseDTO loginTecnico(LoginRequestDTO dto) {

        Tecnico tecnico = tecnicoRepository
                .findByEmail(dto.getEmail())
                .orElseThrow(() ->
                        new BusinessException("E-mail ou senha inválidos"));

        if (!passwordEncoder.matches(dto.getSenha(), tecnico.getSenha())) {
            throw new BusinessException("E-mail ou senha inválidos");
        }

        Long empresaId = tecnico.getEmpresa().getId();

        String token = jwtService.gerarToken(
                tecnico.getEmail(),
                "TECNICO",
                empresaId
        );

        return new LoginResponseDTO(
                tecnico.getId(),
                tecnico.getNome(),
                tecnico.getEmail(),
                "TECNICO",
                empresaId,
                token
        );
    }

    public LoginResponseDTO loginAdministrador(LoginRequestDTO dto) {

        Administrador administrador = administradorRepository
                .findByEmail(dto.getEmail())
                .orElseThrow(() ->
                        new BusinessException("E-mail ou senha inválidos"));

        if (!passwordEncoder.matches(dto.getSenha(), administrador.getSenha())) {
            throw new BusinessException("E-mail ou senha inválidos");
        }

        Long empresaId = administrador.getEmpresa().getId();

        String token = jwtService.gerarToken(
                administrador.getEmail(),
                "ADMIN",
                empresaId
        );

        return new LoginResponseDTO(
                administrador.getId(),
                administrador.getNome(),
                administrador.getEmail(),
                "ADMIN",
                empresaId,
                token
        );
    }
}