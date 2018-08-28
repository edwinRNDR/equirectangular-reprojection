import org.openrndr.Program
import org.openrndr.application
import org.openrndr.configuration
import org.openrndr.draw.ColorBuffer
import org.openrndr.draw.Filter
import org.openrndr.draw.colorBuffer
import org.openrndr.filter.filterWatcherFromUrl
import org.openrndr.math.Matrix44
import org.openrndr.math.Vector3
import org.openrndr.math.transforms.transform
import org.openrndr.resourceUrl

class ReprojectFilter: Filter(watcher= filterWatcherFromUrl(resourceUrl("/shaders/reproject.frag"))){
    var rotX:Double by parameters
    var rotY:Double by parameters
    var rotate: Matrix44 by parameters
}

class Reproject: Program() {
    var drawFunc = {}
    override fun setup() {
        val cb = ColorBuffer.fromUrl("file:data/sphericalMap.jpg")
        val result = colorBuffer(cb.width, cb.height)
        val filter = ReprojectFilter()

        drawFunc = {
            filter.rotate = transform {
                rotate(Vector3.UNIT_X,mouse.position.x)
                rotate(Vector3.UNIT_Z, mouse.position.y)
            }
            filter.apply(cb, result)
            drawer.image(result, result.bounds, drawer.bounds)
        }
    }

    override fun draw() = drawFunc()
}

fun main(args: Array<String>) {
    application(Reproject(), configuration {
        width = 1600
        height = 800
    })
}