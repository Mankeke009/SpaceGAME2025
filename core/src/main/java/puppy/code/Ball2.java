package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;


// Hereda de ObjetoEspacial e implementa Colisionable
public class Ball2 extends ObjetoEspacial implements Colisionable {
    // 1. Atributo para la estrategia
    private MovimientoStrategy movimientoStrategy;
    
    public Ball2(int x, int y, int size, int xSpeed, int ySpeed, Texture tx) {
        super(x, y, xSpeed, ySpeed, tx);
        // Inicializamos la estrategia por defecto
        this.movimientoStrategy = new MovimientoRebote();
        // Corrección de bordes iniciales
    	if (x - size < 0) this.x = x + size;
    	if (x + size > Gdx.graphics.getWidth()) this.x = x - size;
    	if (y - size < 0) this.y = y + size;
    	if (y + size > Gdx.graphics.getHeight()) this.y = y - size;
    	
    	spr.setPosition(this.x, this.y);
    }

    @Override
   protected void mover() {
        movimientoStrategy.mover(this);
    }
   @Override
    protected void comportamientoBordes() {
        if (x + xSpeed < 0 || x + xSpeed + spr.getWidth() > Gdx.graphics.getWidth())
        	xSpeed *= -1;
        if (y + ySpeed < 0 || y + ySpeed + spr.getHeight() > Gdx.graphics.getHeight())
        	ySpeed *= -1;
    }
    @Override
    public Rectangle getArea() {
        return spr.getBoundingRectangle();
    }

    @Override
    public boolean verificarColision(Colisionable otro) {
        return this.getArea().overlaps(otro.getArea());
    }

    public void setXSpeed(int xSpeed) { this.xSpeed = xSpeed; }
    public void setySpeed(int ySpeed) { this.ySpeed = ySpeed; }
    public int getXSpeed() { return (int)xSpeed; }
    public int getySpeed() { return (int)ySpeed; }
    
    // Método específico de Ball2 para rebotar con otras bolas
    public void checkCollision(Ball2 b2) {
        if(verificarColision(b2)){
            if (getXSpeed() == 0) setXSpeed(getXSpeed() + b2.getXSpeed()/2);
            if (b2.getXSpeed() == 0) b2.setXSpeed(b2.getXSpeed() + getXSpeed()/2);
        	setXSpeed(-getXSpeed());
            b2.setXSpeed(-b2.getXSpeed());
            
            if (getySpeed() == 0) setySpeed(getySpeed() + b2.getySpeed()/2);
            if (b2.getySpeed() == 0) b2.setySpeed(b2.getySpeed() + getySpeed()/2);
            setySpeed(-getySpeed());
            b2.setySpeed(-b2.getySpeed()); 
        }
    }
}