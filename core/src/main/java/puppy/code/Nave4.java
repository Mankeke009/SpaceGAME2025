package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;



public class Nave4 extends ObjetoEspacial implements Colisionable {
    
    private boolean destruida = false;
    private int vidas = 3;
    private boolean herido = false;
    private int tiempoHeridoMax = 50;
    private int tiempoHerido;
    private Sound sonidoHerido;
    private Sound soundBala;
    private Texture txBala;
    private float shipSpeed = 3.0f;
    private float rotacion = 0;

    public Nave4(int x, int y, Texture tx, Sound soundChoque, Texture txBala, Sound soundBala) {
        super(x, y, 0, 0, tx); // xSpeed e ySpeed iniciales en 0
        this.sonidoHerido = soundChoque;
        this.soundBala = soundBala;
        this.txBala = txBala;
        
        spr.setOriginCenter();
        spr.setBounds(x, y, 45, 45);
    }

    @Override
    public void update() {
        // La nave actualiza su lógica principalmente en draw() por el input,
        // pero podríamos moverlo aquí en el futuro.
    }
    
    // Método draw personalizado que maneja el input
    public void draw(SpriteBatch batch, PantallaJuego juego) {
        // --- Apuntar con Mouse ---
        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        juego.camera.unproject(mousePos); 
        float shipX = x + spr.getOriginX(); // Usamos 'x' heredada
        float shipY = y + spr.getOriginY(); // Usamos 'y' heredada
        float deltaX = mousePos.x - shipX;
        float deltaY = mousePos.y - shipY;
        float angle = MathUtils.atan2(deltaY, deltaX) * MathUtils.radiansToDegrees;
        float targetRotation = angle - 90; 
        this.rotacion = MathUtils.lerpAngleDeg(this.rotacion, targetRotation, 0.15f);
        spr.setRotation(this.rotacion);

        if (!herido) {
            // --- Movimiento Stop-on-Release ---
            xSpeed = 0; // Usamos variable heredada
            ySpeed = 0; // Usamos variable heredada

            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) xSpeed = -shipSpeed;
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) xSpeed = shipSpeed;
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) ySpeed = -shipSpeed;
            if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) ySpeed = shipSpeed;

            // Bordes
            if (x + xSpeed < 0 || x + xSpeed + spr.getWidth() > Gdx.graphics.getWidth()) xSpeed = 0;
            if (y + ySpeed < 0 || y + ySpeed + spr.getHeight() > Gdx.graphics.getHeight()) ySpeed = 0;
            
            x += xSpeed;
            y += ySpeed;
            spr.setPosition(x, y);
            spr.draw(batch);
        } else {
            spr.setX(x + MathUtils.random(-2, 2));
            spr.draw(batch);
            spr.setX(x);
            tiempoHerido--;
            if (tiempoHerido <= 0) herido = false;
        }
        
        // --- Disparo ---
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            float bulletSpeed = 5.0f;
            float angleRad = (this.rotacion + 90) * MathUtils.degreesToRadians; 
            int bXSpeed = (int) (MathUtils.cos(angleRad) * bulletSpeed);
            int bYSpeed = (int) (MathUtils.sin(angleRad) * bulletSpeed);
            
            // Spawn en centro
            float spawnX = x + spr.getOriginX() - txBala.getWidth()/2f;
            float spawnY = y + spr.getOriginY() - txBala.getHeight()/2f;
            
            Bullet bala = new Bullet(spawnX, spawnY, bXSpeed, bYSpeed, txBala);
            juego.agregarBala(bala);
            soundBala.play();
        }
    }

    @Override
    public Rectangle getArea() {
        return spr.getBoundingRectangle();
    }

    @Override
    public boolean verificarColision(Colisionable otro) {
        return !herido && this.getArea().overlaps(otro.getArea());
    }
    
    public boolean checkCollision(Ball2 b) {
        if(verificarColision(b)){
            // Rebote simple al chocar
            if (xSpeed == 0) xSpeed += b.getXSpeed()/2;
            if (b.getXSpeed() == 0) b.setXSpeed(b.getXSpeed() + (int)xSpeed/2);
            xSpeed = -xSpeed;
            b.setXSpeed(-b.getXSpeed());
            
            if (ySpeed == 0) ySpeed += b.getySpeed()/2;
            if (b.getySpeed() == 0) b.setySpeed(b.getySpeed() + (int)ySpeed/2);
            ySpeed = -ySpeed;
            b.setySpeed(-b.getySpeed());
            
            vidas--;
            herido = true;
            tiempoHerido = tiempoHeridoMax;
            sonidoHerido.play();
            if (vidas <= 0) destruida = true;
            return true;
        }
        return false;
    }
    
    public boolean estaDestruido() { return !herido && destruida; }
    public boolean estaHerido() { return herido; }
    public int getVidas() { return vidas; }
    public void setVidas(int vidas2) { vidas = vidas2; }
}