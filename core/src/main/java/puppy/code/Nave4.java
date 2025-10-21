package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;



public class Nave4 {
	
    private boolean destruida = false;
    private int vidas = 3;
    private float xVel = 0;
    private float yVel = 0;
    private float shipSpeed = 2.0f;
    private Sprite spr;
    private Sound sonidoHerido;
    private Sound soundBala;
    private Texture txBala;
    private boolean herido = false;
    private int tiempoHeridoMax=50;
    private int tiempoHerido;
    private float rotacion;
    
    public Nave4(int x, int y, Texture tx, Sound soundChoque, Texture txBala, Sound soundBala) {
    	sonidoHerido = soundChoque;
    	this.soundBala = soundBala;
    	this.txBala = txBala;
    	spr = new Sprite(tx);
    	spr.setPosition(x, y);
        spr.setOriginCenter();
    	spr.setBounds(x, y, 45, 45);

    }
    public void draw(SpriteBatch batch, PantallaJuego juego){
        float x =  spr.getX();
        float y =  spr.getY();
        
        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        
        juego.camera.unproject(mousePos);
        
        float shipX = spr.getX() + spr.getOriginX();
        float shipY = spr.getY() + spr.getOriginY();
        
        float deltaX = mousePos.x - shipX;
        float deltaY = mousePos.y - shipY;
        
        float angle = MathUtils.atan2(deltaY, deltaX) * MathUtils.radiansToDegrees;
        float targetRotation = angle - 90; 
        this.rotacion = MathUtils.lerpAngleDeg(this.rotacion, targetRotation, 0.15f);
        spr.setRotation(this.rotacion);
        if (!herido) {
	    if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            xVel = -shipSpeed;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
                xVel = shipSpeed;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
                yVel = -shipSpeed;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
                yVel = shipSpeed;
            }
            if (x + xVel < 0 || x + xVel + spr.getWidth() > Gdx.graphics.getWidth()) {
            xVel = 0; 
            }
            if (y + yVel < 0 || y + yVel + spr.getHeight() > Gdx.graphics.getHeight()) {
                yVel = 0; 
            }
	        
	        // que se mantenga dentro de los bordes de la ventana
	    if (x+xVel < 0 || x+xVel+spr.getWidth() > Gdx.graphics.getWidth())
	        xVel*=-1;
	    if (y+yVel < 0 || y+yVel+spr.getHeight() > Gdx.graphics.getHeight())
                yVel*=-1;
	        
	    spr.setPosition(x+xVel, y+yVel);   
         
            spr.draw(batch);
        } else {
            spr.setX(spr.getX()+MathUtils.random(-2,2));
            spr.draw(batch); 
            spr.setX(x);
            tiempoHerido--;
            if (tiempoHerido<=0) herido = false;
 	}
        // disparo
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
           
          float bulletSpeed = 3.0f; 
          float angleRad = (this.rotacion + 90) * MathUtils.degreesToRadians; 
          int xSpeed = (int) (MathUtils.cos(angleRad) * bulletSpeed);
          int ySpeed = (int) (MathUtils.sin(angleRad) * bulletSpeed);
          float spawnX = spr.getX() + spr.getOriginX() - txBala.getWidth()/2f;
          float spawnY = spr.getY() + spr.getOriginY() - txBala.getHeight()/2f;
          Bullet  bala = new Bullet(spawnX, spawnY, xSpeed, ySpeed, txBala); 
          juego.agregarBala(bala);
	      soundBala.play();
        }
       
    }
      
    public boolean checkCollision(Ball2 b) {
        if(!herido && b.getArea().overlaps(spr.getBoundingRectangle())){
        	// rebote
            if (xVel ==0) xVel += b.getXSpeed()/2;
            if (b.getXSpeed() ==0) b.setXSpeed(b.getXSpeed() + (int)xVel/2);
            xVel = - xVel;
            b.setXSpeed(-b.getXSpeed());
            
            if (yVel ==0) yVel += b.getySpeed()/2;
            if (b.getySpeed() ==0) b.setySpeed(b.getySpeed() + (int)yVel/2);
            yVel = - yVel;
            vidas--;
            herido = true;
  		    tiempoHerido=tiempoHeridoMax;
  		    sonidoHerido.play();
            if (vidas<=0) 
          	    destruida = true; 
            return true;
        }
        return false;
    }
    
    public boolean estaDestruido() {
       return !herido && destruida;
    }
    public boolean estaHerido() {
 	   return herido;
    }
    
    public int getVidas() {return vidas;}
    //public boolean isDestruida() {return destruida;}
    public int getX() {return (int) spr.getX();}
    public int getY() {return (int) spr.getY();}
	public void setVidas(int vidas2) {vidas = vidas2;}
}
