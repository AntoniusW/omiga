package network;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectStreamException;

/**
 * This class is used as a substitute for Functions/Constants
 * whenever they are sent to another node.
 * It uses the more efficient Externalizable version of serialization.
 * 
 * @author Antonius Weinzierl
 */
public class AExternalizedForm implements Externalizable {
    
    Integer value;
    
    public AExternalizedForm() { }
    
    public AExternalizedForm(Integer value) {
        this.value=value;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(value.intValue());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.value=in.readInt();
    }
    
    /*
     * Whenever an object of this type is encountered, we replace it with the
     * corresponding object from the mapping.
     */
    public Object readResolve() throws ObjectStreamException {
        return ANodeImpl.deser_mapping.get(value);
    }
}
