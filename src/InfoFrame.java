import java.awt.Color;

import javax.swing.*;

public class InfoFrame extends JFrame{
	
	private JLabel background;
	private ImageIcon backgroundImage;
	InfoFrame(){
		backgroundImage = new ImageIcon("infoBackground.jpg");
		background = new JLabel();
		
		background.setText("<html><font color='yellow' face='monospace'><b><pre>" +
				"=================================================================\n" +
				"        REPUBLIC NAVAL COMMAND: STRATEGIC BRIEFING\n" +
				"=================================================================\n" +
				"LOGGED IN AS: NAVAL COMMANDER\n" +
				"OFFICER IN CHARGE: Admiral Anakin Skywalker\n" +
				"CURRENT SECTOR: Outer Rim Territory\n" +
				"OBJECTIVE: Intercept and Annihilate Separatist CIS Fleet\n\n" +
				"Attention Officer. A massive Separatist fleet has dropped out of\n" +
				"hyperspace and ambushed our fleet position. General Grievous flag ship\n" +
				"The Malevolence has used its superweapon to disable our scanners.\n" +
				"Read these tactical directives immediately before taking command:\n\n" +
				"-----------------------------------------------------------------\n" +
				"1. REINFORCE THE PERIMETER (FLEET PLACEMENT PHASE)\n" +
				"-----------------------------------------------------------------\n" +
				"Before exchanging turbolaser fire, you must coordinate your ship layout.\n" +
				"Use the 'Set Horizontal' interface toggle to position your hulls safely\n" +
				"within your 10x10 sector grid.\n\n" +
				"Your battle group consists of 4 primary capital ships:\n" +
				" - Venator-class Star Destroyer   [Size: 5 Sectors]\n" +
				" - Acclamator-class Assault Ship  [Size: 4 Sectors]\n" +
				" - Arquitens-class Command Cruiser[Size: 3 Sectors]\n" +
				" - Interceptor Class Corvette     [Size: 2 Sectors]\n\n" +
				"CRITICAL: Ships will be deployed from smallest to largest\n" +
				"CRITICAL: Do not overlap your ship components or position them beyond\n" +
				"outer system grid coordinates.\n\n" +
				"-----------------------------------------------------------------\n" +
				"2. ENGAGE THE SEPARATISTS (COMBAT RULES)\n" +
				"-----------------------------------------------------------------\n" +
				"Once fleets are hidden behind electronic cloaking shields, structural\n" +
				"engagement begins. You and the Separatist Tactical Droid commander will\n" +
				"take turns targeting coordinate vectors (0-9) on the opposing side.\n\n" +
				"Monitor your tactical grid readouts closely:\n" +
				" - DEEP SPACE VOID (Green Check): Empty unscaned space.\n" +
				" - SHIELD COMPROMISED (Red Grid): Direct hit recorded on a hull!\n" +
				" - ENEMY SCANNER WARNING (Black Grid):Your turbolasers fired into empty space.\n" +
				" - CLOAKED AMBIENT (Gray Grid): Your ships' current hidden status.\n\n" +
				"-----------------------------------------------------------------\n" +
				"3. VICTORY CONDITIONS\n" +
				"-----------------------------------------------------------------\n" +
				"To secure a definitive victory for the Galactic Republic, you must target,\n" +
				"track down, and completely sink all 4 Separatist ships.\n\n" +
				"If the Separatist fleet manages to pierce our defenses and turn all of\n" +
				"your vessels into space dust first, the system falls to the Confederacy.\n\n" +
				"Do your duty, Commander. The fate of the Republic rests on your shoulders.\n" +
				"May the Force be with you.\n" +
				"=================================================================</pre></b></font></html>");
		background.setIcon(backgroundImage);
		background.setForeground(Color.white);
		background.setHorizontalTextPosition(JLabel.CENTER);
		this.setSize(1183,2560);
		
		
		this.add(background);
		this.setVisible(true);
	}
}
