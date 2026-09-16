package com.orcamentos.empresa;

public record EmpresaResponse(
        Long id,
        String nome,
        String documento,
        String telefone,
        String email,
        String endereco,
        String logotipoUrl
) {
    public static EmpresaResponse de(Empresa empresa) {
        return new EmpresaResponse(
                empresa.getId(),
                empresa.getNome(),
                empresa.getDocumento(),
                empresa.getTelefone(),
                empresa.getEmail(),
                empresa.getEndereco(),
                empresa.getLogotipoUrl()
        );
    }
}
