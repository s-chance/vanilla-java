package io.github.schance.debug.compiler;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class MemoryFileManager extends ForwardingJavaFileManager<StandardJavaFileManager> {
    private final Map<String, byte[]> classBytes = new HashMap<>();

    public MemoryFileManager(StandardJavaFileManager delegate) {
        super(delegate);
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
        if (kind == JavaFileObject.Kind.CLASS) {
            return new SimpleJavaFileObject(URI.create(className), kind) {
                @Override
                public OutputStream openOutputStream() throws IOException {
                    return new ByteArrayOutputStream() {
                        @Override
                        public void close() throws IOException {
                            classBytes.put(className, toByteArray());
                        }
                    };
                }
            };
        }
        return super.getJavaFileForOutput(location, className, kind, sibling);
    }

    public Map<String, byte[]> getClassBytes() {
        return classBytes;
    }
}
