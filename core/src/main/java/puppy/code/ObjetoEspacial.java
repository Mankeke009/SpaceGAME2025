package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * GM1.4: Clase abstracta que encapsula la lógica común de los objetos del juego:
 * posición, velocidad física, sprite y dibujado.
 */
public abstract class ObjetoEspacial {
    protected float x;
    protected float y;
    protected float xSpeed;
    protected float ySpeed;
    protected Sprite spr;

    public ObjetoEspacial(float x, float y, float xSpeed, float ySpeed, Texture tx) {
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        
        if (tx != null) {
            spr = new Sprite(tx);
            spr.setPosition(x, y);
        }
    }

    // Método común para dibujar (Reutilización de código real)
    public void draw(SpriteBatch batch) {
        spr.draw(batch);
    }
    
   // --- PATRÓN TEMPLATE METHOD (GM2.2) ---

    public final void update() {
        mover();                // Paso 1: Calcular nueva posición
        comportamientoBordes(); // Paso 2: Que hacer si toca un borde
        spr.setPosition(x, y);  // Paso 3: Actualizar sprite 
    }

    // 2. Los "Pasos" que cada hijo debe implementar a su manera
    protected abstract void mover();
    protected abstract void comportamientoBordes();
}
