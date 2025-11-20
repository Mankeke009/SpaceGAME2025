/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package puppy.code;

/**
 *
 * @author Mankeke
 */
public interface JuegoFactory {
    public Nave4 crearNave(int x, int y);
    public Ball2 crearAsteroide(int x, int y, int velX, int velY);
}
