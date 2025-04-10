package ca.deltagis.success.v1.domain.ports.in;

public interface ICommonSeeder<T extends ICommonEntity<T>> {
    /**
     * Seeds the database with data.
     * <p>
     * This method is typically invoked at application startup time in order to
     * initialize the database with some amount of data. The data is usually
     * read from a file, or even generated programmatically.
     */
    void seed();
}
