package xstream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;

public class TestAlias {

	public static void main(String[] args) {
		Blog teamBlog = new Blog(new Author("Guilherme Silveira"));
        teamBlog.add(new Entry("first","My first blog entry."));
        teamBlog.add(new Entry("tutorial",
                "Today we have developed a nice alias tutorial. Tell your friends! NOW!"));

        XStream xstream = new XStream(new JsonHierarchicalStreamDriver());
        xstream.alias("blog", Blog.class);
        xstream.alias("entry", Entry.class);
        xstream.aliasField("author", Blog.class, "writer");
        xstream.addImplicitCollection(Blog.class, "entries");

        xstream.useAttributeFor(Blog.class, "writer");
        xstream.registerConverter(new AuthorConverter());

        System.out.println(xstream.toXML(teamBlog));
	}
}
