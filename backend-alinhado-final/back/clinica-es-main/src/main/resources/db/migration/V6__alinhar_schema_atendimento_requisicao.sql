-- =============================================================================
-- V6: Alinhar schema de atendimento e requisicao_medicacao com o contrato atual
-- =============================================================================

-- ─── ATENDIMENTO ─────────────────────────────────────────────────────────────
ALTER TABLE atendimento
    ADD COLUMN IF NOT EXISTS paciente_id BIGINT;

UPDATE atendimento a
SET paciente_id = p.paciente_id
FROM prontuario p
WHERE a.prontuario_id = p.id
  AND a.paciente_id IS NULL;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.table_constraints
        WHERE constraint_name = 'fk_atendimento_paciente'
    ) THEN
        ALTER TABLE atendimento
            ADD CONSTRAINT fk_atendimento_paciente
            FOREIGN KEY (paciente_id) REFERENCES paciente(id);
    END IF;
END $$;

ALTER TABLE atendimento
    ALTER COLUMN prontuario_id DROP NOT NULL;

-- ─── REQUISICAO_MEDICACAO ────────────────────────────────────────────────────
ALTER TABLE requisicao_medicacao
    ADD COLUMN IF NOT EXISTS quantidade BIGINT NOT NULL DEFAULT 0,
    ADD COLUMN IF NOT EXISTS tipo_requisicao VARCHAR(50),
    ADD COLUMN IF NOT EXISTS profissional_saude_id BIGINT,
    ADD COLUMN IF NOT EXISTS data VARCHAR(50) NOT NULL DEFAULT CURRENT_DATE::text,
    ADD COLUMN IF NOT EXISTS observacao TEXT;

DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'requisicao_medicacao' AND column_name = 'tipo'
    ) THEN
        EXECUTE '
            UPDATE requisicao_medicacao
            SET tipo_requisicao = tipo
            WHERE tipo_requisicao IS NULL
        ';
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.table_constraints
        WHERE constraint_name = 'fk_requisicao_profissional'
    ) THEN
        ALTER TABLE requisicao_medicacao
            ADD CONSTRAINT fk_requisicao_profissional
            FOREIGN KEY (profissional_saude_id) REFERENCES profissional_saude(id);
    END IF;
END $$;
