package puppy.code;

public class HighScore {
    // 1. La única instancia estática (Singleton)
    private static HighScore instance;
    
    // Variable para guardar el récord
    private int highScore;

    // 2. Constructor privado: Nadie desde fuera puede hacer "new HighScoreManager()"
    private HighScore() {
        this.highScore = 0;
    }

    // 3. Método público estático para obtener la única instancia
    public static HighScore getInstance() {
        if (instance == null) {
            instance = new HighScore();
        }
        return instance;
    }

    // --- Métodos de Lógica del Puntaje ---

    public int getHighScore() {
        return highScore;
    }

    // Método que verifica si el puntaje actual es mayor al récord y lo actualiza
    public void updateHighScore(int currentScore) {
        if (currentScore > this.highScore) {
            this.highScore = currentScore;
        }
    }
}