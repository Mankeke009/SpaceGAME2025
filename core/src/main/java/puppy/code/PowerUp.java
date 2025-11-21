package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class PowerUp extends ObjetoEspacial implements Colisionable {

    public PowerUp(float x, float y, float xSpeed, float ySpeed, Texture tx) {
        super(x, y, xSpeed, ySpeed, tx);
    }

    // Template Method: Movimiento suave hacia abajo
    @Override
    protected void mover() {
        x += xSpeed;
        y += ySpeed;
    }

    // Template Method: Si sale de la pantalla, desaparece (no rebota)
    @Override
    protected void comportamientoBordes() {
        // No necesitamos lógica aquí, se eliminará en PantallaJuego si sale del borde
    }

    @Override
    public Rectangle getArea() {
        return spr.getBoundingRectangle();
    }

    @Override
    public boolean verificarColision(Colisionable otro) {
        return this.getArea().overlaps(otro.getArea());
    }
}