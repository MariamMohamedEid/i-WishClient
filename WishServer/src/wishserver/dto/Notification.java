/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wishserver.dto;

/**
 *
 * @author LENOVO
 */
public class Notification {
    private String Context;

    public Notification(String Context) {
        this.Context = Context;
    }

    public String getContext() {
        return Context;
    }

    public void setContext(String Context) {
        this.Context = Context;
    }   
}
