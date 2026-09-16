package com.orcamentos.empresa;

public interface EmpresaService {

    Empresa buscarPorId(Long empresaId);

    Empresa atualizar(Long empresaId, EmpresaRequest request);

    Empresa atualizarLogotipo(Long empresaId, String logotipoUrl);

}
