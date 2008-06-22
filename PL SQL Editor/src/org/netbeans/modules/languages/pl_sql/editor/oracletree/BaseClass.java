/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.languages.pl_sql.editor.oracletree;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.prefs.Preferences;
import javax.swing.JFileChooser;
import org.netbeans.modules.languages.pl_sql.editor.Utils;
import org.openide.cookies.EditCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.util.Exceptions;
import org.openide.windows.WindowManager;

/**
 *
 * @author SUMsoft
 */
public class BaseClass implements EditCookie {

    private String Owner = null,  ObjectName,  ObjectSource,  Status;
    private ObjectTypes ObjectType;
    private Date Created,  LastDDLTime;
    private Preferences ParentPref;

    public BaseClass(String OOwner, String OName, ObjectTypes OType,
            Date OCreated, Date OLastDDLTime, String OStatus, Preferences pref) {
        Owner = OOwner;
        ObjectName = OName;
        ObjectType = OType;
        Created = OCreated;
        LastDDLTime = OLastDDLTime;
        Status = OStatus;
        ParentPref = pref;
    }

    public Preferences getPreferencesRoot() {
        return ParentPref.node(this.toString());
    }

    @Override
    public String toString() {
        if (Owner != null) {
            return Owner + '.' + ObjectName;
        } else {
            return ObjectName;
        }
    }

    public String getLocalFile() {
        return getPreferencesRoot().get("LocalFile", "");
    }

    public void setLocalFile(String LocalFile) {
        getPreferencesRoot().put("LocalFile", LocalFile);
    }

    public String getObjectSource() {
        return ObjectSource;
    }

    public void setObjectSource(String OSource) {
        ObjectSource = OSource;
    }

    public Date getCreated() {
        return Created;
    }

    public Date getLastDDLTime() {
        return LastDDLTime;
    }

    public ObjectTypes getObjectType() {
        return ObjectType;
    }

    public String getStatus() {
        return Status;
    }

    public boolean isValid() {
        return Status.compareToIgnoreCase("VALID") == 0;
    }

    private File returnLocalFile() {
        File f = new File(getLocalFile());
        try {
            String s = f.getPath();
            if (s == null || s.equalsIgnoreCase("") || f.isDirectory() || !f.isFile()) {
                throw new IOException();
            }
        } catch (IOException ex) {
            JFileChooser fc = new JFileChooser();
            String filename = this.toString() + '.' + Utils.getFileExtensionByType(ObjectType);
            fc.setSelectedFile(new File(filename));
            int ret = fc.showSaveDialog(WindowManager.getDefault().getMainWindow());
            f = fc.getSelectedFile();
            if (f == null || ret == JFileChooser.CANCEL_OPTION) {
                return null;
            }
        }
        return f;
    }

    public void edit() {
        File selFile = returnLocalFile();
        if (selFile == null) {
            return;
        }
        FileObject file;
        DataObject dob;
        try {
            FileWriter fw = new FileWriter(selFile);
            fw.write(this.ObjectSource);
            fw.close();
            //file = Repository.getDefault().getDefaultFileSystem().findResource(selFile.getCanonicalPath());
            file = FileUtil.toFileObject(selFile);
            dob = DataObject.find(file);
            EditCookie cookie = dob.getCookie(EditCookie.class);
            if (cookie != null) {
                cookie.edit();
            }
            setLocalFile(selFile.getCanonicalPath());
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    //MyClass theInstance = (MyClass) cookie.instanceCreate();
    //JFileChooser chooser = new JFileChooser();
    //int returnVal = chooser.showSaveDialog(WindowManager.getDefault().getMainWindow());


    //Lookup lookup = MimeLookup.getLookup("text/pl_sql");
    //EditorKit ek = CloneableEditorSupport.getEditorKit("text/pl_sql");
    //if (ek != null) {
    //throw new UnsupportedOperationException("ek.getContentType()");
    //}
    }
}
