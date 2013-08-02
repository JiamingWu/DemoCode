package xstream;

import com.thoughtworks.xstream.converters.SingleValueConverter;

public class AuthorConverter implements SingleValueConverter {
	
	public boolean canConvert(Class type) {
		return type.equals(Author.class);
	}

	public String toString(Object obj) {
		return ((Author) obj).getName();
	}

	public Object fromString(String name) {
		return new Author(name);
	}
}
