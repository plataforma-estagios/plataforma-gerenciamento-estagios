DO $$
DECLARE
    fk_to_drop TEXT;
BEGIN
    IF to_regclass('public.candidatura') IS NULL THEN
        RETURN;
    END IF;

    -- Remove relacionamentos legados via usuario_id
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'public'
          AND table_name = 'candidatura'
          AND column_name = 'usuario_id'
    ) THEN
        FOR fk_to_drop IN
            SELECT c.conname
            FROM pg_constraint c
            JOIN pg_class t ON t.oid = c.conrelid
            JOIN pg_namespace n ON n.oid = t.relnamespace
            JOIN pg_attribute a ON a.attrelid = t.oid AND a.attnum = ANY (c.conkey)
            WHERE c.contype = 'f'
              AND n.nspname = 'public'
              AND t.relname = 'candidatura'
              AND a.attname = 'usuario_id'
        LOOP
            EXECUTE format('ALTER TABLE public.candidatura DROP CONSTRAINT IF EXISTS %I', fk_to_drop);
        END LOOP;

        ALTER TABLE public.candidatura DROP COLUMN IF EXISTS usuario_id;
    END IF;

    -- Garante coluna candidato_id obrigatoria
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'public'
          AND table_name = 'candidatura'
          AND column_name = 'candidato_id'
    ) THEN
        ALTER TABLE public.candidatura
            ALTER COLUMN candidato_id SET NOT NULL;
    END IF;

    -- Ajusta checks para os enums atuais da aplicacao
    ALTER TABLE public.candidatura DROP CONSTRAINT IF EXISTS candidatura_status_check;
    ALTER TABLE public.candidatura
        ADD CONSTRAINT candidatura_status_check
        CHECK (status IN ('SUBMETIDA', 'EM_ANALISE', 'ENTREVISTA', 'PROXIMA_ETAPA', 'APROVADA', 'REPROVADA', 'CANCELADA'));

    ALTER TABLE public.candidatura DROP CONSTRAINT IF EXISTS candidatura_resultado_entrevista_check;
    ALTER TABLE public.candidatura
        ADD CONSTRAINT candidatura_resultado_entrevista_check
        CHECK (
            resultado_entrevista IS NULL
            OR resultado_entrevista IN ('SUBMETIDA', 'EM_ANALISE', 'ENTREVISTA', 'PROXIMA_ETAPA', 'APROVADA', 'REPROVADA', 'CANCELADA')
        );
END $$;
