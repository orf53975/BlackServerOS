/**
 * 	This file is part of Kayak.
 *
 *	Kayak is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU Lesser General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *
 *	Kayak is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *
 *	You should have received a copy of the GNU Lesser General Public License
 *	along with Kayak.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.github.kayak.ui.statistics;

import com.github.kayak.core.Bus;
import com.github.kayak.core.BusChangeListener;
import com.github.kayak.ui.projects.ProjectManager;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.netbeans.api.settings.ConvertAsProperties;

/**
 *
 * @author Jan-Niklas Meier <dschanoeh@googlemail.com>
 */
@ConvertAsProperties(dtd = "-//com.github.kayak.ui.statistics//BusStatistics//EN",
autostore = false)
@TopComponent.Description(preferredID = "BusStatisticsTopComponent",
iconBase="org/tango-project/tango-icon-theme/16x16/apps/utilities-system-monitor.png",
persistenceType = TopComponent.PERSISTENCE_ONLY_OPENED)
@TopComponent.Registration(mode = "statistics", openAtStartup = false)
public final class BusStatisticsTopComponent extends TopComponent {

    private static final Logger logger = Logger.getLogger(BusStatisticsTopComponent.class.getName());
    private static BusStatisticsTopComponent instance;

    private StatisticsTableModel model = new StatisticsTableModel();
    private Bus bus;

    private BusChangeListener listener = new BusChangeListener() {

        @Override
        public void connectionChanged() {

        }

        @Override
        public void nameChanged(String name) {
            setName(NbBundle.getMessage(BusStatisticsTopComponent.class, "CTL_BusStatisticsTopComponent") + " - " + bus.toString());
        }

        @Override
        public void destroyed() {
            close();
        }

        @Override
        public void descriptionChanged() {

        }

        @Override
        public void aliasChanged(String string) {
            setName(NbBundle.getMessage(BusStatisticsTopComponent.class, "CTL_BusStatisticsTopComponent") + " - " + bus.toString());
        }
    };

    public BusStatisticsTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(BusStatisticsTopComponent.class, "CTL_BusStatisticsTopComponent"));
        setToolTipText(NbBundle.getMessage(BusStatisticsTopComponent.class, "HINT_BusStatisticsTopComponent"));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        jTable1.setFont(new java.awt.Font("Monospaced", 0, 14)); // NOI18N
        jTable1.setModel(model);
        jScrollPane1.setViewportView(jTable1);

        add(jScrollPane1);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized BusStatisticsTopComponent getDefault() {
        if (instance == null) {
            instance = new BusStatisticsTopComponent();
        }
        return instance;
    }


    public void setBus(Bus bus) {
        this.bus = bus;
        bus.addBusChangeListener(listener);

        bus.registerStatisticsReceiver(model);
        bus.enableStatistics(model.getInterval());

        setName(NbBundle.getMessage(BusStatisticsTopComponent.class, "CTL_BusStatisticsTopComponent") + " - " + bus.toString());
    }

    void writeProperties(java.util.Properties p) {
        p.setProperty("version", "1.0");

        p.setProperty("busName", bus.getName());
        ProjectManager manager = ProjectManager.getGlobalProjectManager();
        p.setProperty("projectName", manager.getOpenedProject().getName());
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");

        String busName = p.getProperty("busName");
        String projectName = p.getProperty("projectName");

        logger.log(Level.INFO, "Trying to restore statistics view with project {0} and bus {1}", new String[]{projectName, busName});

        Bus newBus = ProjectManager.getGlobalProjectManager().findBus(projectName, busName);

        if (newBus == null) {
            this.close();
            return;
        }

        setBus(newBus);
    }
}