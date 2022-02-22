import javax.swing.JFrame;

/**
 * 
 * @author Omar Fawzy
 *
 */

public abstract class SGUI extends JFrame{
	public abstract void addProcess(Process p);
	public abstract void updateProcess(Process process, int len);
	public abstract void updateProcess(int index, int len);
	public abstract void updateProcess(Process process, int from, int to);
}
