/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.umg.sistema.estudiantes.modelo;
public class Cliente extends Persona {
    private String direccion;
    
    @Override
    public String mostrarInformacion() {
        return "Cliente: " + getNombre() + " - NIT: " + getNit();
    }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
}
