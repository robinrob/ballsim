package sim;

/**
 * DoubleSimParameter extends DoubleParameter to implement a DoubleParameter
 * with minimum and maximum values.
 * 
 * @author Robin Smith
 * @version 1 (03/11/2010)
 */

public class DoubleSimParameter extends DoubleParameter
{
	private double minValue;
	private double maxValue;
	
	public DoubleSimParameter(String name, double value, double minValue,
	                            double maxValue, boolean isAdjustable)
	{
		super(name, value, isAdjustable);
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
		
	/**
	 * Sets the value of the DoubleSetupParameter.
	 * @param value the new value.
	 */
	public void setValue(double value)
	{
		if (value > minValue && value < maxValue) {
			this.value = value;
		}
	}
	
	/**
	 * Increases the value of the DoubleSimParameter by two.
	 */
	public void incValue()
	{
		if (value < 0.0001) {
			value = 0.0001;
		}
		else {
			double newValue = value * 2;
			
			if (newValue < maxValue) {
				value = newValue;
			}
			else {
				value = maxValue;
			}
		}
	}
	
	/**
	 * Decreases the value of the DoubleSimParameter by two.
	 */
	public void decValue()
	{
		double newValue = value / 2;
			
		if (newValue > minValue) {
				value = newValue;
		}
		else {
			value = minValue;
		}
	}
	
}
