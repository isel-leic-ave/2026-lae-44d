import java.util.ArrayList;
import java.util.List;

class Test {
	public static void main(String[] args){
		List<String> names = new ArrayList<>();
		List raw = names;
		raw.add(123); // unchecked operation (throws a exception)
		String first = names.get(0); // runtime cast check occurs here

	}
}
