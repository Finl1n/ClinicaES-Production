-- =============================================================================
-- V5: Corrigir schema de medicacao e atendimento para alinhar com as entidades
-- =============================================================================

-- ─── MEDICACAO ───────────────────────────────────────────────────────────────
-- Remove colunas legadas incompatíveis e adiciona os campos reais do modelo

-- Adiciona campos reais (se ainda não existirem)
ALTER TABLE medicacao
    ADD COLUMN IF NOT EXISTS quantidade     BIGINT       NOT NULL DEFAULT 0,
    ADD COLUMN IF NOT EXISTS unidade_medida VARCHAR(100) NOT NULL DEFAULT 'unidade',
    ADD COLUMN IF NOT EXISTS ativo          BOOLEAN      NOT NULL DEFAULT TRUE;

-- Remove colunas legadas que causavam NOT NULL constraint violations
ALTER TABLE medicacao
    DROP COLUMN IF EXISTS fornecedor,
    DROP COLUMN IF EXISTS armazenamento_medicacao,
    DROP COLUMN IF EXISTS estoque,
    DROP COLUMN IF EXISTS data_aquisicao,
    DROP COLUMN IF EXISTS validade;

-- Remove colunas de status legado (substituído por ativo)
-- Mantém status se existir para não quebrar dados antigos — apenas torna nullable
ALTER TABLE medicacao
    ALTER COLUMN status DROP NOT NULL;

-- ─── ATENDIMENTO ─────────────────────────────────────────────────────────────
-- Adiciona os campos do contrato atual com o frontend

ALTER TABLE atendimento
    ADD COLUMN IF NOT EXISTS descricao         TEXT,
    ADD COLUMN IF NOT EXISTS observacoes       TEXT,
    ADD COLUMN IF NOT EXISTS data_atendimento  VARCHAR(50),
    ADD COLUMN IF NOT EXISTS status_atendimento VARCHAR(50);

-- Torna campos legados opcionais (alguns eram NOT NULL no schema original)
ALTER TABLE atendimento
    ALTER COLUMN data_inicio    DROP NOT NULL,
    ALTER COLUMN tipo           DROP NOT NULL;

-- ─── PACIENTE ────────────────────────────────────────────────────────────────
-- Adiciona colunas de vínculo se ainda não existirem (V4 pode não ter rodado)

ALTER TABLE paciente
    ADD COLUMN IF NOT EXISTS vinculo_paciente VARCHAR(50),
    ADD COLUMN IF NOT EXISTS vinculo_nome     VARCHAR(255),
    ADD COLUMN IF NOT EXISTS escola_id        BIGINT,
    ADD COLUMN IF NOT EXISTS unidade_id       BIGINT,
    ADD COLUMN IF NOT EXISTS categoria_paciente VARCHAR(50);

-- Atualiza coluna categoria para o nome correto da entidade
-- (o schema original tinha 'categoria'; a entidade Java usa 'categoria_paciente')
DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'paciente' AND column_name = 'categoria'
    ) AND NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'paciente' AND column_name = 'categoria_paciente'
    ) THEN
        ALTER TABLE paciente RENAME COLUMN categoria TO categoria_paciente;
    END IF;
END $$;

-- FK escola_id
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.table_constraints
        WHERE constraint_name = 'fk_paciente_escola'
    ) THEN
        ALTER TABLE paciente
            ADD CONSTRAINT fk_paciente_escola
            FOREIGN KEY (escola_id) REFERENCES centro_custo(id);
    END IF;
END $$;

-- FK unidade_id
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.table_constraints
        WHERE constraint_name = 'fk_paciente_unidade'
    ) THEN
        ALTER TABLE paciente
            ADD CONSTRAINT fk_paciente_unidade
            FOREIGN KEY (unidade_id) REFERENCES centro_custo(id);
    END IF;
END $$;
