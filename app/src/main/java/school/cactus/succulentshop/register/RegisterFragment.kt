package school.cactus.succulentshop.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import school.cactus.succulentshop.R
import school.cactus.succulentshop.auth.JwtStore
import school.cactus.succulentshop.databinding.FragmentRegisterBinding
import school.cactus.succulentshop.infra.BaseFragment

class RegisterFragment : BaseFragment() {
    private var _binding: FragmentRegisterBinding? = null

    private val binding get() = _binding!!

    override val viewModel: RegisterViewModel by viewModels {
        RegisterViewModelFactory(
            store = JwtStore(requireContext()),
            repository = RegisterRepository()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().title = getString(R.string.sign_up)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}