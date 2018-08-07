package org.myapp.sheduler.data;

import java.util.Comparator;

class TaskComparator implements Comparator<Task> {

	@Override
	public int compare(Task o1, Task o2) {
		int result;
		if (o1 == null && o2 == null) {
			result = 0;
		} else if (o1 != null && o2 == null) {
			result = 1;
		} else if (o1 == null && o2 != null) {
			result = -1;
		} else {
			String key1 = o1.getName() + "_" + o1.getId();
			String key2 = o2.getName() + "_" + o2.getId();
			result = key1.compareTo(key2);
		}
		return result;
	}
}
