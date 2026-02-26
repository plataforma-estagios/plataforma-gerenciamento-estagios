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
          AND column_name = 'tipo_vaga'
          AND data_type IN ('smallint', 'integer', 'bigint')
    ) THEN
        ALTER TABLE vagas
            ALTER COLUMN tipo_vaga TYPE VARCHAR(50)
            USING CASE tipo_vaga
                WHEN 0 THEN 'ESTAGIO'
                WHEN 1 THEN 'TRAINEE'
                WHEN 2 THEN 'EMPREGO'
                ELSE 'ESTAGIO'
            END;
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
