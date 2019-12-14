package dhbw.swingchat.instance;

import static java.util.Objects.hash;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Group} object.
 */
public class GroupTest
{

    @Test
    public void should_find_user_by_name()
    {
        Group group = new Group("Test", new User("Hans"));

        assertTrue(group.contains("Hans"));
    }

    @Test
    public void should_find_user_by_object()
    {
        Group group = new Group("Test", new User("Hans"));

        assertTrue(group.contains(new User("Hans")));
    }

    @Test
    public void should_compare_groups()
    {
        Group group1 = new Group("Test", new User("Hans"), new User("Peter"));
        Group group2 = new Group("Test", new User("Peter"), new User("Hans"));

        assertThat(group1, equalTo(group2));
    }

    @Test
    public void should_not_be_equal_when_different_object()
    {
        Group group = new Group("Test", new User("Hans"), new User("Peter"));

        assertThat(group, not(new Object()));
    }

    @Test
    public void should_not_be_equal_with_different_name()
    {
        Group group1 = new Group("Test", new User("Hans"), new User("Peter"));
        Group group2 = new Group("test", new User("Peter"), new User("Hans"));

        assertThat(group1, not(group2));
    }

    @Test
    public void should_not_be_equal_with_different_user()
    {
        Group group1 = new Group("Test", new User("Hans"), new User("Peter"));
        Group group2 = new Group("Test", new User("Hans"), new User("Max"));

        assertThat(group1, not(group2));
    }

    @Test
    public void should_not_be_equal_with_missing_user()
    {
        Group group1 = new Group("Test", new User("Hans"), new User("Peter"));
        Group group2 = new Group("Test", new User("Hans"));

        assertThat(group1, not(group2));
    }

    @Test
    public void should_not_be_equal_with_additional_user()
    {
        Group group1 = new Group("Test", new User("Hans"), new User("Peter"));
        Group group2 = new Group("Test", new User("Hans"), new User("Peter"), new User("Max"));

        assertThat(group1, not(group2));
    }

    @Test
    public void should_return_usernames_when_calling_toString()
    {
        Group group = new Group("Test", new User("Hans"), new User("Peter"));

        assertThat(group.toString(), equalTo("Hans, Peter"));
    }

    @Test
    public void should_remove_user()
    {
        User user = new User("Hans");
        Group group = new Group("Test", user);

        group.remove(user);

        assertFalse(group.contains("Hans"));
    }

    @Test
    public void should_hash_group()
    {
        User user = new User("Hans");
        List<User> users = new ArrayList<>();
        users.add(user);
        Group group = new Group("Test", user);

        assertThat(group.hashCode(), equalTo(hash("Test", users)));
    }
}
