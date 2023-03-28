package components.common

import Form
import FormField
import react.FC
import react.dom.html.InputType
import react.dom.html.ReactHTML.details
import react.dom.html.ReactHTML.summary


val AlgoSettingsForm = FC<AlgoSettingsProps> { props ->

    details {
        summary {
            +"Genetic settings"
        }
        Form {
            fields = listOf(
                FormField( "Population Size" , InputType.number, props.algoSettings.populationSize.toString()),
                FormField( "Chromosome Size" , InputType.number, props.algoSettings.chromosomeSize.toString()),
                FormField( "Mutation probability (0 to 1)" , InputType.number, props.algoSettings.mutationProbability.toString()),
                FormField( "Elitism (0 to 1)" , InputType.number, props.algoSettings.elitismPercent.toString())
                )
            onChange = { println(fields) }
        }
    }
    details {
        summary {
            +"Mars Landing"
        }
        Form {
            fields = listOf(
                FormField( "Speed Max" , InputType.number, props.algoSettings.populationSize.toString()),
                FormField( "X Speed weight" , InputType.number, props.algoSettings.chromosomeSize.toString()),
                FormField( "Y Speed weight" , InputType.number, props.algoSettings.mutationProbability.toString()),
                FormField( "Rotate weight" , InputType.number, props.algoSettings.rotateWeight.toString()),
                FormField( "Distance weight" , InputType.number, props.algoSettings.distanceWeight.toString()),
                FormField( "Speed weight (Crashing)" , InputType.number, props.algoSettings.crashSpeedWeight.toString()),
            )
            onChange = { println(fields) }
        }

    }

}