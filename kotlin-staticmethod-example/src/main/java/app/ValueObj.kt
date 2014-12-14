package app

import kotlin.platform.platformStatic

public class ValueObj private (val value: String) {
  class object {
    platformStatic fun valueOf(value: String) = ValueObj(value)
  }
}

