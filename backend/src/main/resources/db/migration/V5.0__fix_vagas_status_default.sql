DO $$
BEGIN
    IF to_regclass('public.vaga') IS NOT NULL AND to_regclass('public.vagas') IS NULL THEN
        ALTER TABLE vaga RENAME TO vagas;
    END IF;
END $$;

DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'public'
          AND table_name = 'vagas'
          AND column_name = 'status'
          AND data_type IN ('smallint', 'integer', 'bigint')
    ) THEN
        ALTER TABLE vagas
            ALTER COLUMN status TYPE VARCHAR(50)
            USING CASE status
                WHEN 0 THEN 'EM_ABERTO'
                WHEN 1 THEN 'FECHADA'
                WHEN 2 THEN 'CANCELADA'
                WHEN 3 THEN 'ENCERRADA'
                ELSE 'EM_ABERTO'
            END;
    END IF;
END $$;

UPDATE vagas
SET status = 'EM_ABERTO'
WHERE status IS NULL
   OR status NOT IN ('EM_ABERTO', 'FECHADA', 'CANCELADA', 'ENCERRADA');

ALTER TABLE vagas
    ALTER COLUMN status SET DEFAULT 'EM_ABERTO';

ALTER TABLE vagas
    ALTER COLUMN status SET NOT NULL;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'vagas_status_check'
    ) THEN
        ALTER TABLE vagas
            ADD CONSTRAINT vagas_status_check
            CHECK (status IN ('EM_ABERTO', 'FECHADA', 'CANCELADA', 'ENCERRADA'));
    END IF;
END $$;
