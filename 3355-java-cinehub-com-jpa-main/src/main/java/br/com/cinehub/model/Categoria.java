package br.com.cinehub.model;

public enum Categoria {
    ACAO("Action", "Ação"),
    ROMANCE("Romance", "Romance"),
    COMEDIA("Comedy", "Comédia"),
    DRAMA("Drama", "Drama"),
    CRIME("Crime", "Crime"),
    ANIMACAO("Animation", "Animação"),
    AVENTURA("Adventure", "Aventura"),
    FANTASIA("Fantasy", "Fantasia"),
    FICCAO_CIENTIFICA("Sci-Fi", "Ficção Científica"),
    TERROR("Horror", "Terror"),
    MISTERIO("Mystery", "Mistério"),
    DOCUMENTARIO("Documentary", "Documentário"),
    BIOGRAFIA("Biography", "Biografia"),
    HISTORIA("History", "História"),
    MUSICAL("Music", "Musical"),
    ESPORTE("Sport", "Esporte"),
    GUERRA("War", "Guerra"),
    FAROESTE("Western", "Faroeste"),
    THRILLER("Thriller", "Thriller"),
    FAMILIA("Family", "Família");

    private String categoriaOmdb;
    private String categoriaPortugues;

    Categoria(String categoriaOmdb, String categoriaPortugues) {
        this.categoriaOmdb = categoriaOmdb;
        this.categoriaPortugues = categoriaPortugues;
    }

    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOmdb.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        // Fallback — tenta pelo português também
        return fromPortugues(text);
    }

    public static Categoria fromPortugues(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaPortugues.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        return DRAMA; // fallback seguro ao invés de lançar exceção
    }
}