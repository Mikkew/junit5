package com.mms.test.springboot.app.services;

import com.mms.test.springboot.app.models.Cuenta;

import java.math.BigDecimal;
import java.util.List;

public interface CuentaService {

    public List<Cuenta> findAll();

    public Cuenta findById(Long id);

    public Cuenta save(Cuenta cuenta);

    public Integer revisarTotalTransferencias(Long bancoId);

    public BigDecimal revisarSaldo(Long cuentaId);

    public void transferir(Long cuentaOrigenId, Long cuentaDestinoId, BigDecimal monto, Long bancoId);

    public void deteleById(Long id);
}
