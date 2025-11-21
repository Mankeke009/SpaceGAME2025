/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package puppy.code;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

/**
 *
 * @author Mankeke
 */
public class FabricaNivel1 implements JuegoFactory {

    @Override
    public Nave4 crearNave(int x, int y) {
        // Aquí centralizamos la creación de la nave y sus assets para el Nivel 1
        Texture txNave = new Texture(Gdx.files.internal("MainShip3.png"));
        Texture txBala = new Texture(Gdx.files.internal("Rocket2.png"));
        Sound sndHerido = Gdx.audio.newSound(Gdx.files.internal("hurt.ogg"));
        Sound sndBala = Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3"));
        
        return new Nave4(x, y, txNave, sndHerido, txBala, sndBala);
    }

    @Override
    public Ball2 crearAsteroide(int x, int y, int velX, int velY) {
        // Aquí centralizamos la creación de asteroides para el Nivel 1
        Texture txAsteroide = new Texture(Gdx.files.internal("aGreyMedium4.png"));
        // Tamaño base 20 + random(10) lo manejamos fuera o fijo aqui, 
        // usaremos un tamaño estándar para la fábrica o pasamos size.
        // Para simplificar, usaremos tamaño 30 por defecto.
        return new Ball2(x, y, 30, velX, velY, txAsteroide);
    }
    public PowerUp crearPowerUp(int x, int y, int velX, int velY) {
        // Usamos la textura de la bala (Rocket2) para el PowerUp por ahora,
        // para que se distinga de los asteroides.
        Texture txPowerUp = new Texture(Gdx.files.internal("heartt.png")); 
        return new PowerUp(x, y, velX, velY, txPowerUp);
    }
}