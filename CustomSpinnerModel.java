package gui;

//code from https://stackoverflow.com/questions/10353087/set-step-for-a-dateeditor-in-a-jspinner
//used to give custom steps to my date spinner
//code courtesy of gthanop

import java.awt.GridLayout;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
//custom spinner code to get the 30 minute intervals
public class CustomSpinnerModel extends JPanel {
	public static class StepperSpinnerDateModel extends SpinnerDateModel {
		public final SpinnerDateModel internal; // We let the internal SpinnerDateModel do the work for us.
		private final int step; // The number of steps to increment and decrement per click.
		private Object currentValue; // Needed to get restored each time getPreviousValue and getNextValue is called.

		public StepperSpinnerDateModel(final Date value, final Comparable start, final Comparable end,
				final int calendarField, final int step) {
			internal = new SpinnerDateModel(value, start, end, calendarField);
			if (step <= 0)
				throw new IllegalArgumentException("Non positive step.");
			this.step = step;
			currentValue = internal.getValue();
		}

		public StepperSpinnerDateModel(final int step) {
			this(new Date(), null, null, Calendar.DAY_OF_MONTH, step);
		}

		@Override
		public Object getValue() {
			return currentValue;
		}

		@Override
		public void setValue(final Object value) {
			internal.setValue(value);
			currentValue = value;
			fireStateChanged(); // Important step for the spinner to get updated each time the model's value
								// changes.
		}

		@Override
		public Object getNextValue() {
			Object next = null;
			for (int i = 0; i < step; ++i) { // Calculate step next values:
				next = internal.getNextValue();
				internal.setValue(next); // We have to set the next value internally, in order to recalculate the
											// next-next value in the next loop.
			}
			internal.setValue(currentValue); // Restore current value.
			return next;
		}

		@Override
		public Object getPreviousValue() {
			Object prev = null;
			for (int i = 0; i < step; ++i) { // Calculate step previous values:
				prev = internal.getPreviousValue();
				internal.setValue(prev); // We have to set the previous value internally, in order to recalculate the
											// previous-previous value in the next loop.
			}
			internal.setValue(currentValue); // Restore current value.
			return prev;
		}
	}

}