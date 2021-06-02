import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.ResourceBundle;

public class SystemLoggerDemo {

	public static void main(String[] args) {
		ResourceBundle bundle = ResourceBundle.getBundle("SystemLoggerDemo$ResourceBundleImpl");
		{
			Logger logger = System.getLogger("demo");
			if (logger.isLoggable(Level.INFO)) {
				logger.log(Level.INFO, "Hello World");
			}

			logger.log(Level.INFO, () -> "Supplier");

			logger.log(Level.INFO, "{0} + {1} = {2}", 1, 2, 3);

			logger.log(Level.INFO, bundle, "hello");
		}
		{
			Logger logger = System.getLogger("demo-with-bundle", bundle);
			if (logger.isLoggable(Level.INFO)) {
				logger.log(Level.INFO, "Hello World");
			}

			logger.log(Level.INFO, "hello");

			logger.log(Level.INFO, "bind", 12345);
		}
	}

	public static class ResourceBundleImpl extends ResourceBundle {

		private final Map<String, String> map = Map.of(
				"hello", "Hello, ResourceBundle!",
				"bind", "{0} is bound parameter");

		@Override
		protected Object handleGetObject(String key) {
			return map.get(key);
		}

		@Override
		public Enumeration<String> getKeys() {
			return Collections.enumeration(map.keySet());
		}
	}
}
