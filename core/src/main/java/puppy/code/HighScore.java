package puppy.code;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class HighScore {
    
    private static HighScore instance;
    private int highScore;

    private HighScore() {
        prefs = Gdx.app.getPreferences("SpaceNavData");
        this.highScore = prefs.getInteger("highscore", 0);
    }
   
    private Preferences prefs;
   
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

    public void updateHighScore(int currentScore) {
        if (currentScore > this.highScore) {
            this.highScore = currentScore;
            
            // 3. Guardamos el nuevo récord en disco
            prefs.putInteger("highscore", this.highScore);
            
            // 4. ¡Importante! flush() obliga a escribir los cambios inmediatamente
            prefs.flush(); 
        }
    }
}