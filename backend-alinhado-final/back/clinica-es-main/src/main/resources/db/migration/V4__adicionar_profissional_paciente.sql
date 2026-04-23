-- MIGRATION: V4 — Adicionar chave estrangeira profissional_id na tabela paciente
-- Esta migration deve ser executada após V3

ALTER TABLE paciente
    ADD COLUMN profissional_id BIGINT;

ALTER TABLE paciente
    ADD CONSTRAINT fk_paciente_profissional
        FOREIGN KEY (profissional_id)
        REFERENCES profissional_saude(id);

-- Define o primeiro profissional existente como dono dos pacientes legados (se houver)
UPDATE paciente p
SET profissional_id = (SELECT id FROM profissional_saude LIMIT 1)
WHERE profissional_id IS NULL;

-- Tornar a coluna NOT NULL após popular dados legados
ALTER TABLE paciente
    ALTER COLUMN profissional_id SET NOT NULL;
