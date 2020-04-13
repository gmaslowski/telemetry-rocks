package com.telemetry.rocks.c4

import com.structurizr.Workspace
import com.structurizr.io.plantuml.PlantUMLWriter
import com.structurizr.model.InteractionStyle.Asynchronous
import com.structurizr.model.Location.External
import com.structurizr.model.SoftwareSystem
import com.structurizr.model.Tags.*
import com.structurizr.view.Shape
import com.structurizr.view.Shape.Circle
import java.io.StringWriter

fun main(args: Array<String>) {


    val workspace = Workspace("Telemetry Rocks Platform", "")
    val model = workspace.model

    // software systems
    val telemetryRocks: SoftwareSystem = model.addSoftwareSystem("Telemetry Rocks", "All your sim telemetry in one place.")

    // external systems
    val simRacingGame = model.addSoftwareSystem(External, "SimRacing Game", "F1 / Asseto Corsa / rFactor")
    simRacingGame.addTags(EXTERNAL_SYSTEM_TAG)

    simRacingGame.uses(telemetryRocks, "Sends telemetry data.")

    // actors
    val simDriver = model.addPerson("Sim Racer", "SimRacing Game player.")

    // containers
    val telemetryAgent = telemetryRocks.addContainer("srg-agent", "SimRacingGame Agent. Listens for telemetry data.", "")
    val telemetryReceiver = telemetryRocks.addContainer("telemetry-receiver", "Passes through RAW telemetry data from WS to Kafka.", "")
    val telemetryProcessor = telemetryRocks.addContainer("telemetry-processor", "Prepares incoming telemetry data for further processing.", "Kafka Streams")
    val telemetryClient = telemetryRocks.addContainer("client", "Frontend application for telemetry viewing.", "")
    val telemetryLapTimes = telemetryRocks.addContainer("lap-times", "", "")

    // interactions
    simDriver.uses(simRacingGame, "Plays a simulator.")
    simDriver.uses(telemetryRocks, "Analyzes telemetry.")
    simDriver.uses(telemetryAgent, "Configures.")
    simDriver.uses(telemetryClient, "")

    simRacingGame.uses(telemetryAgent, "Sends telemetry data.")

    telemetryAgent.uses(telemetryReceiver, "", "WebSocket")

    telemetryReceiver.uses(telemetryProcessor, "", "Kafka", Asynchronous)

    telemetryProcessor.uses(telemetryLapTimes, "")

    telemetryClient.uses(telemetryLapTimes, "")


    val views = workspace.views
    val contextView = views.createSystemContextView(telemetryRocks, "SystemContext", "An example of a System Context diagram.")
    contextView.addAllSoftwareSystems()
    contextView.addAllPeople()

    val containerView = views.createContainerView(telemetryRocks, "Container", "An example of a System Container diagram.")
    containerView.addAllSoftwareSystems()
    containerView.addAllPeople()
    containerView.addAllContainersAndInfluencers()

    val styles = views.configuration.styles
    styles.addElementStyle(SOFTWARE_SYSTEM).background(BLUE).color(WHITE)
    styles.addElementStyle(CONTAINER).background(BLUE).color(WHITE)
    styles.addElementStyle(EXTERNAL_SYSTEM_TAG).background(GRAY).color(WHITE).shape(Circle)
    styles.addElementStyle(PERSON).background(DARK_BLUE).color(WHITE).shape(Shape.Person)
    styles.addElementStyle(QUEUE_TAG).background(BLUE).color(WHITE).shape(Shape.Cylinder)
    styles.addElementStyle(DATABASE_TAG).background(BLUE).color(WHITE).shape(Shape.Cylinder)

    val stringWriter = StringWriter()
    val plantUMLWriter = PlantUMLWriter()
    plantUMLWriter.addSkinParam("rectangleFontColor", WHITE)
    plantUMLWriter.addSkinParam("rectangleStereotypeFontColor", WHITE)
    plantUMLWriter.write(workspace, stringWriter)
    println(stringWriter.toString())
}

