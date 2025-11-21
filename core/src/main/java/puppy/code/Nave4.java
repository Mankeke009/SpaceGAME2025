package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
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
        super(x, y, 0, 0, tx);
        this.sonidoHerido = soundChoque;
        this.soundBala = soundBala;
        this.txBala = txBala;
        
        spr.setOriginCenter();
        spr.setBounds(x, y, 45, 45);
    }

    // --- TEMPLATE METHOD: Pasos implementados ---

    @Override
    protected void mover() {
        // Aplicamos la velocidad actual
        x += xSpeed;
        y += ySpeed;
    }

    @Override
    protected void comportamientoBordes() {
        // Mantenerse dentro de la pantalla (Clamp)
        if (x + xSpeed < 0 || x + xSpeed + spr.getWidth() > Gdx.graphics.getWidth()) xSpeed = 0;
        if (y + ySpeed < 0 || y + ySpeed + spr.getHeight() > Gdx.graphics.getHeight()) ySpeed = 0;
    }

    // --------------------------------------------

    public void draw(SpriteBatch batch, PantallaJuego juego) {
        // --- 1. Lógica de Input (Mouse) ---
        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        juego.camera.unproject(mousePos); 
        float shipX = x + spr.getOriginX();
        float shipY = y + spr.getOriginY();
        float deltaX = mousePos.x - shipX;
        float deltaY = mousePos.y - shipY;
        float angle = MathUtils.atan2(deltaY, deltaX) * MathUtils.radiansToDegrees;
        float targetRotation = angle + 260 ; 
        this.rotacion = MathUtils.lerpAngleDeg(this.rotacion, targetRotation, 1.0f);
        spr.setRotation(this.rotacion);

        if (!herido) {
            // --- 2. Lógica de Input (Teclado) ---
            xSpeed = 0; 
            ySpeed = 0; 

            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) xSpeed = -shipSpeed;
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) xSpeed = shipSpeed;
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) ySpeed = -shipSpeed;
            if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) ySpeed = shipSpeed;

            // --- 3. Ejecutar Template Method ---
            // Esto llama a mover() -> comportamientoBordes() -> setPosition()
            this.update(); 
            
            spr.draw(batch);
        } else {
            // Lógica de herido (vibración visual)
            spr.setX(x + MathUtils.random(-2, 2));
            spr.draw(batch);
            spr.setX(x);
            tiempoHerido--;
            if (tiempoHerido <= 0) herido = false;
        }
        
        // --- 4. Disparo ---
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            float bulletSpeed = 5.0f;
            float angleRad = (this.rotacion + 90) * MathUtils.degreesToRadians; 
            int bXSpeed = (int) (MathUtils.cos(angleRad) * bulletSpeed);
            int bYSpeed = (int) (MathUtils.sin(angleRad) * bulletSpeed);
            
            float spawnX = x + spr.getOriginX() - txBala.getWidth()/2f;
            float spawnY = y + spr.getOriginY() - txBala.getHeight()/2f;
            
            Bullet bala = new Bullet(spawnX, spawnY, bXSpeed, bYSpeed, txBala);
            juego.agregarBala(bala);
            soundBala.play();
        }
    }

    @Override
    public Rectangle getArea() { return spr.getBoundingRectangle(); }

    @Override
    public boolean verificarColision(Colisionable otro) {
        return !herido && this.getArea().overlaps(otro.getArea());
    }
    
    // ... (checkCollision y getters/setters se mantienen igual) ...
    public boolean checkCollision(Ball2 b) {
        if(verificarColision(b)){
            // Rebote simple al chocar (mantenemos la lógica de siempre)
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
    public void ganarVida() {
        vidas++;
        // Opcional: Reproducir sonido de "power up" si tuvieras uno
    }
    public boolean estaDestruido() { return !herido && destruida; }
    public boolean estaHerido() { return herido; }
    public int getVidas() { return vidas; }
    public void setVidas(int vidas2) { vidas = vidas2; }
}