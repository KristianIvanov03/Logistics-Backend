package com.company.logistics.service;

import com.company.logistics.model.clientaccount.ClientResponseDTO;
import com.company.logistics.model.entities.ClientAccount;
import com.company.logistics.model.clientaccount.ClientRegisterRequest;
import com.company.logistics.model.company.AuthenticationRequest;
import com.company.logistics.model.company.AuthenticationResponse;
import com.company.logistics.model.entities.Company;
import com.company.logistics.repository.ClientAccountRepository;
import com.company.logistics.repository.CompanyRepository;
import com.company.logistics.utils.AuthenticationService;
import com.company.logistics.utils.GlobalMapper;
import com.company.logistics.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ClientAccountService {
    private final ClientAccountRepository clientAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final CompanyRepository companyRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationService authenticationService;

    public AuthenticationResponse registerClientAccount(ClientRegisterRequest request) {
        Company company = companyRepository.findById(request.getCompanyId()).orElseThrow(() -> new UsernameNotFoundException("Company not found"));
        ClientAccount account = ClientAccount.builder()
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .role(request.getRole())
                .companyId(company).build();

        ClientAccount clientAccount = clientAccountRepository.save(account);
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", request.getRole());
        extraClaims.put("id", clientAccount.getId());
        var jwtToken = jwtUtil.generateToken(extraClaims,account);
        return AuthenticationResponse.builder()
                .token(jwtToken).build();

    }

    public AuthenticationResponse loginClient(AuthenticationRequest loginRequest){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getName(),
                        loginRequest.getPassword()
                )
        );
        var user = clientAccountRepository.findByName(loginRequest.getName()).orElseThrow();
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", user.getRole());
        extraClaims.put("id", user.getId());
        var authToken = jwtUtil.generateToken(extraClaims, user);
        return AuthenticationResponse.builder().token(authToken).build();
    }

    public ClientResponseDTO getClientInfo(){
        ClientAccount clientAccount = authenticationService.getAuthenticatedClient();
        return GlobalMapper.buildClientResponse(clientAccount);
    }
}
