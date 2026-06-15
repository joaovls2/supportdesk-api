package com.supportdesk.service;

import com.supportdesk.entity.Administrador;
import com.supportdesk.repository.AdministradorRepository;
import com.supportdesk.dto.LoginRequestDTO;
import com.supportdesk.dto.LoginResponseDTO;
import com.supportdesk.entity.Tecnico;
import com.supportdesk.entity.Usuario;
import com.supportdesk.exception.BusinessException;
import com.supportdesk.repository.TecnicoRepository;
import com.supportdesk.repository.UsuarioRepository;
import com.supportdesk.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final TecnicoRepository tecnicoRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AdministradorRepository administradorRepository;

    public AuthService(
            UsuarioRepository usuarioRepository,
            TecnicoRepository tecnicoRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService, AdministradorRepository administradorRepository) {

        this.usuarioRepository = usuarioRepository;
        this.tecnicoRepository = tecnicoRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.administradorRepository = administradorRepository;
    }

    public LoginResponseDTO loginUsuario(LoginRequestDTO dto) {

        Usuario usuario = usuarioRepository
                .findByEmail(dto.getEmail())
                .orElseThrow(() ->
                        new BusinessException("E-mail ou senha inválidos"));

        if (!passwordEncoder.matches(dto.getSenha(), usuario.getSenha())) {
            throw new BusinessException("E-mail ou senha inválidos");
        }

        String token = jwtService.gerarToken(
                usuario.getEmail(),
                "USUARIO"
        );

        return new LoginResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                "USUARIO",
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

        String token = jwtService.gerarToken(
                tecnico.getEmail(),
                "TECNICO"
        );

        return new LoginResponseDTO(
                tecnico.getId(),
                tecnico.getNome(),
                tecnico.getEmail(),
                "TECNICO",
                token
        );
    }

    public LoginResponseDTO loginAdministrador(
            LoginRequestDTO dto) {

        Administrador administrador =
                administradorRepository
                        .findByEmail(dto.getEmail())
                        .orElseThrow(() ->
                                new BusinessException(
                                        "E-mail ou senha inválidos"
                                ));

        if (!passwordEncoder.matches(
                dto.getSenha(),
                administrador.getSenha())) {

            throw new BusinessException(
                    "E-mail ou senha inválidos"
            );
        }

        String token = jwtService.gerarToken(
                administrador.getEmail(),
                "ADMIN"
        );

        return new LoginResponseDTO(
                administrador.getId(),
                administrador.getNome(),
                administrador.getEmail(),
                "ADMIN",
                token
        );
    }
}