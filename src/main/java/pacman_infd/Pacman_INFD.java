/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pacman_infd;

import pacman_infd.utils.SoundManager;
import pacman_infd.games.View;

/**
 * @author Marinus
 */
public class Pacman_INFD {
    public static void main(String[] args) {
        SoundManager.loadSoundFiles();
        View view = new View();
        view.setVisible(true);
    }
}
