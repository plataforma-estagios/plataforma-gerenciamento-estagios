DO $$
DECLARE
    fk_to_drop TEXT;
BEGIN
    IF to_regclass('public.vaga') IS NOT NULL AND to_regclass('public.vagas') IS NULL THEN
        ALTER TABLE vaga RENAME TO vagas;
    END IF;

    -- Remove FKs antigas/inválidas da coluna empresa_id que não apontam para empresa(id)
    FOR fk_to_drop IN
        SELECT c.conname
        FROM pg_constraint c
        JOIN pg_class t ON t.oid = c.conrelid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        JOIN pg_attribute a ON a.attrelid = t.oid AND a.attnum = ANY (c.conkey)
        JOIN pg_class rt ON rt.oid = c.confrelid
        WHERE c.contype = 'f'
          AND n.nspname = 'public'
          AND t.relname = 'vagas'
          AND a.attname = 'empresa_id'
          AND rt.relname <> 'empresa'
    LOOP
        EXECUTE format('ALTER TABLE public.vagas DROP CONSTRAINT IF EXISTS %I', fk_to_drop);
    END LOOP;

    -- Garante a FK correta vagas(empresa_id) -> empresa(id)
    IF to_regclass('public.empresa') IS NOT NULL
       AND NOT EXISTS (
        SELECT 1
        FROM pg_constraint c
        JOIN pg_class t ON t.oid = c.conrelid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        JOIN pg_attribute a ON a.attrelid = t.oid AND a.attnum = ANY (c.conkey)
        JOIN pg_class rt ON rt.oid = c.confrelid
        WHERE c.contype = 'f'
          AND n.nspname = 'public'
          AND t.relname = 'vagas'
          AND a.attname = 'empresa_id'
          AND rt.relname = 'empresa'
    ) THEN
        ALTER TABLE public.vagas
            ADD CONSTRAINT fk_vagas_empresa
            FOREIGN KEY (empresa_id) REFERENCES public.empresa (id);
    END IF;
END $$;
