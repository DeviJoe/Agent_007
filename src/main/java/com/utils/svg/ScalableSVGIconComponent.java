package com.utils.svg;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import javax.swing.Icon;
import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.svg.SVGDocument;

/** 
 * Простой компонент, отрисовывающий svg на все свое пространство,
 * одновременно является (реализует интерфейс) Icon
 * (Взято из https://gist.github.com/lindenb/1003235,
 *  original src from http://forums.oracle.com/forums/thread.jspa?threadID=1348087&tstart=840)
 */
public class ScalableSVGIconComponent extends Component implements Icon {
    
    private GraphicsNode svgIcon = null;

    /**
     * Создание объекта ScalableSVGIconComponent из источника url (загрузка)
     * @param url 
     * @throws Exception
     */
    public ScalableSVGIconComponent(String url) throws Exception {
        String xmlParser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory df = new SAXSVGDocumentFactory(xmlParser);
        SVGDocument doc = df.createSVGDocument(url);
        UserAgent userAgent = new UserAgentAdapter();
        DocumentLoader loader = new DocumentLoader(userAgent);
        BridgeContext ctx = new BridgeContext(userAgent, loader);
        ctx.setDynamicState(BridgeContext.DYNAMIC);
        GVTBuilder builder = new GVTBuilder();
        svgIcon = builder.build(ctx, doc);
        this.setSize(
            (int) Math.round(svgIcon.getPrimitiveBounds().getWidth()),
            (int) Math.round(svgIcon.getPrimitiveBounds().getHeight())
        );
        /*
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent ev) {
                repaint();
            }
        });
        */
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);        
        paintIcon(this, g, 0, 0);
    }
    
    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        if (svgIcon == null)
            return;
        
        double scaleX = this.getWidth() / svgIcon.getPrimitiveBounds().getWidth();
        double scaleY = this.getHeight() / svgIcon.getPrimitiveBounds().getHeight();
        double scale = Math.min(scaleX, scaleY);
        AffineTransform transform = new AffineTransform(scale, 0.0, 0.0, scale, x, y);
        svgIcon.setTransform(transform);
        svgIcon.paint((Graphics2D) g);
    }

    @Override
    public int getIconWidth() {
        return this.getWidth();
    }

    @Override
    public int getIconHeight() {
        return this.getHeight();
    }
}
