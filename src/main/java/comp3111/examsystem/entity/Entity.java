package comp3111.examsystem.entity;

/**
 * CLass specifically for storing all entity (data classes)
 */
public class Entity implements java.io.Serializable, Comparable<Entity> {
    /**
     * Id or key of the entity to be identified in the database
     */
    protected Long id = 0L;

    /**
     * Access the id of the entity
     * @return Id of the entity
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the entity id
     * @param id Id for setting
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Default constructor for entity
     */
    public Entity() {
        super();
    }

    /**
     * Constructor for initializing id in the entity
     */
    public Entity(Long id) {
        super();
        this.id = id;
    }

    /**
     * Compare 2 different entities by their id
     * @param o Another entity for comparison
     * @return Integer indicating comparison result. >0 = bigger than, =0 = equal, <0 = smaller than
     */
    public int compareTo(Entity o) {
        return Long.compare(this.id, o.id);
    }
}
