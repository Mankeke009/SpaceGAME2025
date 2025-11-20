package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;


public class Bullet extends ObjetoEspacial implements Colisionable {
    private boolean destroyed = false;

    public Bullet(float x, float y, int xSpeed, int ySpeed, Texture tx) {
        super(x, y, xSpeed, ySpeed, tx);
    }

    @Override
    public void update() {
        x += xSpeed;
        y += ySpeed;
        spr.setPosition(x, y);
        
        // Se marca como destruida si sale de la pantalla
        if (x < 0 || x > Gdx.graphics.getWidth() || y < 0 || y > Gdx.graphics.getHeight()) {
            destroyed = true;
        }
    }
    
    @Override
    public Rectangle getArea() {
        return spr.getBoundingRectangle();
    }

    @Override
    public boolean verificarColision(Colisionable otro) {
        return this.getArea().overlaps(otro.getArea());
    }

    public boolean isDestroyed() { return destroyed; }
    
    // Método específico para interactuar con asteroides en PantallaJuego
    public boolean checkCollision(Ball2 b2) {
        if(verificarColision(b2)){
            this.destroyed = true;
            return true;
        }
        return false;
    }
}