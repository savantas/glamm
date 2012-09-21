package gov.lbl.glamm.client.experiment.util;

/**
 * Interface used to execute commands generically (as within generic search algorithms).
 * 
 * @author DHAP Digital, Inc - angie
 *
 * @param <T>
 */
public interface Executor<T> {
	public void execute(T obj);
}
