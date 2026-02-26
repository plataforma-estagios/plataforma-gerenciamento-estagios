DO $$ 
DECLARE
    status_permitidos TEXT[] := ARRAY['EM_ABERTO', 'FECHADA', 'CANCELADA', 'ENCERRADA'];
    default_status TEXT := 'EM_ABERTO';
BEGIN
    IF to_regclass('public.vaga') IS NOT NULL AND to_regclass('public.vagas') IS NULL THEN
        ALTER TABLE vaga RENAME TO vagas;
    END IF;

    IF EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'vagas' AND column_name = 'status' 
        AND data_type NOT IN ('character varying', 'text')
    ) THEN
        ALTER TABLE vagas 
            ALTER COLUMN status TYPE VARCHAR(50) 
            USING CASE status::text 
                WHEN '0' THEN status_permitidos[1]
                WHEN '1' THEN status_permitidos[2]
                WHEN '2' THEN status_permitidos[3]
                WHEN '3' THEN status_permitidos[4]
                ELSE default_status
            END;
    END IF;

    UPDATE vagas 
    SET status = default_status 
    WHERE status IS NULL OR NOT (status = ANY(status_permitidos));

    ALTER TABLE vagas ALTER COLUMN status SET DEFAULT default_status;
    ALTER TABLE vagas ALTER COLUMN status SET NOT NULL;

    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'vagas_status_check') THEN
        ALTER TABLE vagas 
            ADD CONSTRAINT vagas_status_check 
            CHECK (status = ANY(status_permitidos));
    END IF;

END $$;