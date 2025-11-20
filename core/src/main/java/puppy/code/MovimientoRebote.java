/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package puppy.code;

public class MovimientoRebote implements MovimientoStrategy {

    @Override
    public void mover(ObjetoEspacial objeto) {
        objeto.x += objeto.xSpeed;
        objeto.y += objeto.ySpeed;
    }
}