ALTER TABLE profissional_saude
    ADD COLUMN IF NOT EXISTS formacao VARCHAR(255),
    ADD COLUMN IF NOT EXISTS dias_atendimento VARCHAR(255),
    ADD COLUMN IF NOT EXISTS turnos_atendimento VARCHAR(255),
    ADD COLUMN IF NOT EXISTS data_cadastro DATE;

UPDATE profissional_saude
SET data_cadastro = CURRENT_DATE
WHERE data_cadastro IS NULL;

CREATE TABLE IF NOT EXISTS uso_medicacao (
    id BIGSERIAL PRIMARY KEY,
    atendimento_id BIGINT NOT NULL REFERENCES atendimento(id),
    prontuario_id BIGINT NOT NULL REFERENCES prontuario(id),
    medicamento_id BIGINT REFERENCES medicacao(id),
    quantidade BIGINT NOT NULL,
    dosagem VARCHAR(255)
);
