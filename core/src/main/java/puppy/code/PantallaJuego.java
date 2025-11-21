package puppy.code;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.ScreenUtils;
//import com.badlogic.gdx.graphics.OrthographicCamera;
//import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PantallaJuego extends PantallaBase {

	private SpriteBatch batch;
	private Sound explosionSound;
	private Music gameMusic;
	private int score;
	private int ronda;
	private int velXAsteroides; 
	private int velYAsteroides; 
	private int cantAsteroides;
	private Nave4 nave;
	
	private ArrayList<Ball2> balls1 = new ArrayList<>();
	private ArrayList<Ball2> balls2 = new ArrayList<>();
	private ArrayList<Bullet> balas = new ArrayList<>();
        private ArrayList<PowerUp> powerups = new ArrayList<>();
        // --- GM2.4: Atributo para la fábrica ---
        private JuegoFactory factory;
        private boolean isPaused = false;
	public PantallaJuego(SpaceNavigation game, int ronda, int vidas, int score,  
			int velXAsteroides, int velYAsteroides, int cantAsteroides) {
		
		super(game);
		
		this.ronda = ronda;
		this.score = score;
		this.velXAsteroides = velXAsteroides;
		this.velYAsteroides = velYAsteroides;
		this.cantAsteroides = cantAsteroides;
		
        // --- GM2.4: Inicializamos la fábrica ---
        // (En un juego más grande, esta fábrica podría venir como parámetro del constructor)
        this.factory = new FabricaNivel1();
        
		batch = game.getBatch();
		camera.setToOrtho(false, 800, 640);
		
		//inicializar assets de la pantalla (música fondo y explosión siguen aquí por ser globales de la pantalla)
		explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));
		explosionSound.setVolume(1,0.5f);
		gameMusic = Gdx.audio.newMusic(Gdx.files.internal("piano-loops.wav")); 
		
		gameMusic.setLooping(true);
		gameMusic.setVolume(0.5f);
		gameMusic.play();
		
	    // --- GM2.4: Usamos la fábrica para crear la Nave ---
        // ANTES: nave = new Nave4(..., new Texture(...), ...);
        // AHORA:
	    nave = factory.crearNave(Gdx.graphics.getWidth()/2-50, 30);
	    
	    nave.setVidas(vidas);
        
        // --- GM2.4: Usamos la fábrica para crear Asteroides ---
        Random r = new Random();
        for (int i = 0; i < cantAsteroides; i++) {
            // ANTES: Ball2 bb = new Ball2(..., new Texture(...));
            // AHORA:
	        Ball2 bb = factory.crearAsteroide(
                r.nextInt((int)Gdx.graphics.getWidth()),
	  	        50 + r.nextInt((int)Gdx.graphics.getHeight()-50),
	  	        velXAsteroides + r.nextInt(4), 
                velYAsteroides + r.nextInt(4)
            );
	        balls1.add(bb);
	  	    balls2.add(bb);
	  	}
	}
	public void dibujaEncabezado() {
		CharSequence str = "Vidas: "+nave.getVidas()+" Ronda: "+ronda;
		game.getFont().getData().setScale(2f);		
		game.getFont().draw(batch, str, 10, 30);
		game.getFont().draw(batch, "Score:"+this.score, Gdx.graphics.getWidth()-150, 30);
		game.getFont().draw(batch, "HighScore:" + HighScore.getInstance().getHighScore(), Gdx.graphics.getWidth()/2-100, 30);
	}
	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0, 1);
		batch.begin();
		dibujaEncabezado();

		// 1. Detectar tecla ESC para pausar/reanudar
		if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ESCAPE)) {
			isPaused = !isPaused;
		}

		// 2. Si NO está en pausa, actualizamos la lógica del juego
		if (!isPaused) {
			
			if (!nave.estaHerido()) {
				// Colisiones entre balas y asteroides
				for (int i = 0; i < balas.size(); i++) {
					Bullet b = balas.get(i);
					b.update();

					for (int j = 0; j < balls1.size(); j++) {
						if (b.checkCollision(balls1.get(j))) {
							explosionSound.play();
							balls1.remove(j);
							balls2.remove(j);
							j--;
							score += 10;
						}
					}

					if (b.isDestroyed()) {
						balas.remove(b);
						i--;
					}
				}

				// Actualizar movimiento de asteroides
				for (Ball2 ball : balls1) {
					ball.update();
				}
                                // --- Lógica Power-Ups ---
            
                                // 1. Generar Power-Up aleatorio (probabilidad baja: 0.2% por frame)
                                if (new Random().nextInt(1000) < 2) { 
                                    PowerUp pu = factory.crearPowerUp(
                                        new Random().nextInt(Gdx.graphics.getWidth()), 
                                        Gdx.graphics.getHeight(), 
                                        0, -3 // Cae verticalmente
                                    );
                                    powerups.add(pu);
                                }

                                // 2. Actualizar y verificar colisión
                                for (int i = 0; i < powerups.size(); i++) {
                                    PowerUp pu = powerups.get(i);
                                    pu.update(); // Template Method moviendo el powerup

                                    // Verificar si la nave lo recoge
                                    if (pu.verificarColision(nave)) {
                                        nave.ganarVida(); // Efecto: Vida Extra
                                        powerups.remove(i); // Desaparece
                                        i--;
                                        // Opcional: Sumar puntos extra también
                                        score += 50; 
                                    }
                                    // Eliminar si sale de pantalla
                                    else if (pu.y < 0) {
                                        powerups.remove(i);
                                        i--;
                                    }
                                }
                                // ------------------------
				// Colisiones entre asteroides
				for (int i = 0; i < balls1.size(); i++) {
					Ball2 ball1 = balls1.get(i);
					for (int j = 0; j < balls2.size(); j++) {
						Ball2 ball2 = balls2.get(j);
						if (i < j) {
							ball1.checkCollision(ball2);
						}
					}
				}
			}
            
            // Si está destruida, game over
            if (nave.estaDestruido()) {
                HighScore.getInstance().updateHighScore(score);
                Screen ss = new PantallaGameOver(game);
                ss.resize(1200, 800);
                game.setScreen(ss);
                dispose();
            }
            
            // Nivel completado
            if (balls1.size() == 0) {
                Screen ss = new PantallaJuego(game, ronda + 1, nave.getVidas(), score,
                        velXAsteroides + 3, velYAsteroides + 3, cantAsteroides + 10);
                ss.resize(1200, 800);
                game.setScreen(ss);
                dispose();
            }
		}

		// 3. Dibujamos SIEMPRE (esté en pausa o no) para ver el juego congelado
		for (Bullet b : balas) {
			b.draw(batch);
		}
                for (PowerUp pu : powerups) {
                    pu.draw(batch);
                }
		nave.draw(batch, this);

		for (int i = 0; i < balls1.size(); i++) {
			Ball2 b = balls1.get(i);
			b.draw(batch);
            
            // Solo verificamos choque con la nave si NO está en pausa
            if (!isPaused) {
                if (nave.checkCollision(b)) {
                    balls1.remove(i);
                    balls2.remove(i);
                    i--;
                }
            }
		}
        
        // 4. Si está en pausa, dibujamos el cartel
        if (isPaused) {
            game.getFont().draw(batch, "PAUSA", Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() / 2);
        }

		batch.end();
	}
    
        public boolean agregarBala(Bullet bb) {
            return balas.add(bb);
        }
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		gameMusic.play();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		this.explosionSound.dispose();
		this.gameMusic.dispose();
	}
   
}
